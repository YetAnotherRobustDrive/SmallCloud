import RefreshToken from "../token/RefreshToken";

export default async function GetDownloadAesFile(targetID, setter, after, filename, key) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    
    try {
        const download = async () => {
            return new Promise((resolve, reject) => {
                const xhr = new XMLHttpRequest();
                xhr.responseType = 'blob';
                xhr.open('GET', localStorage.getItem("API_SERVER") + 'files/' + targetID, true);
                xhr.onprogress = (e) => {
                    const percentage = (e.loaded / e.total) * 100;
                    setter(percentage);
                }
                xhr.onreadystatechange = async () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        const res = xhr.response;
                        if (xhr.status !== 200) {      
                            setter(0);
                            resolve(res);
                        }
                        else {
                            try {
                                await window.electron.saveBlob('data/' + filename, new Blob([res]));
                                await window.electron.decryptFile('data/' + filename, key);
                                const blob = await window.electron.getFromLocal('data/' + filename.split('.')[0] + '.' + filename.split('.')[1]);
                                const url = window.URL.createObjectURL(blob);
                                const link = document.createElement('a');
                                link.href = url;
                                link.setAttribute('download', filename.split('.')[0] + '.' + filename.split('.')[1]);
                                document.body.appendChild(link);
                                link.click();
                                link.parentNode.removeChild(link);

                                const tRes = {
                                    "status": 200,
                                    "data": res
                                }
                                resolve(tRes);
                                
                            } catch (error) {
                                console.log(error);
                                resolve({
                                    "status": 500,
                                    "data": error
                                });
                            }
                        }
                    }
                }
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
                xhr.send();
            });
        }

        const res = await download();
        if (res.status === 200) {
            await window.electron.clearFolder('data');
            after();
            return [true, res.data];  //성공
        }
        else {
            throw res; //실패
        }
    } catch (e) {
        after();
        if (e.data.message.includes("File not found")) {
            return [false, "암호화 키가 일치하지 않습니다."]; //실패 후 처리
        }
        return [false, "다운로드에 실패했습니다.\n다시 시도해주세요."]; //실패 후 처리
    }
}