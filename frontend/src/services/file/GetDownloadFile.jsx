import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function GetDownloadFile(targetID, setter, after, filename) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    

    try {
        const upload = async () => {
            return new Promise((resolve, reject) => {
                const xhr = new XMLHttpRequest();
                xhr.responseType = 'blob';
                xhr.open('GET', configData.API_SERVER + 'files/' + targetID, true);
                xhr.onprogress = (e) => {
                    const percentage = (e.loaded / e.total) * 100;
                    setter(percentage);
                }
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        const res = xhr.response;
                        if (xhr.status !== 200) {      
                            setter(0);
                            resolve(res);
                        }
                        else {   
                            const url = window.URL.createObjectURL(new Blob([res]));

                            const link = document.createElement('a');
                            link.href = url;
                            link.setAttribute('download', filename);
                            document.body.appendChild(link);
                            link.click();
                            link.parentNode.removeChild(link);  

                            const tRes = {
                                "status": 200,
                                "data": res
                            }
                            resolve(tRes);
                        }
                    }
                }
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
                xhr.send();
            });
        }

        const res = await upload();
        console.log(res.data);
        if (res.status === 200) {
            after(true);
            return [true, res.data];  //성공
        }
        else {
            throw res; //실패
        }
    } catch (e) {
        return [false, "업로드에 실패했습니다.\n다시 시도해주세요."]; //실패 후 처리
    }
}