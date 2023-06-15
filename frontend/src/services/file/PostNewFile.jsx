import RefreshToken from "../token/RefreshToken";
 

export default async function PostNewFile(value, setter, after) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    

    try {
        const upload = async () => {
            return new Promise((resolve, reject) => {
                const xhr = new XMLHttpRequest();
                xhr.open('POST', localStorage.getItem("API_SERVER") + 'files', true);
                xhr.upload.addEventListener("progress", (e) => {
                    const percentage = (e.loaded / e.total) * 100;
                    setter(percentage);
                });
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        const res = JSON.parse(xhr.responseText);
                        if (xhr.status !== 200) {           
                            setter(0);
                            resolve(res);
                        }
                        else {
                            const tRes = {
                                "status": 200,
                                "data": res
                            }
                            resolve(tRes);
                        }
                    }
                }
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
                xhr.send(value);
            });
        }

        const res = await upload();
        if (res.status === 200) {
            after();
            console.log(res.data);
            return [true, res.data];  //성공
        }
        else {
            throw res; //실패
        }
    } catch (e) {
        console.log(e);
        return [false, "업로드에 실패했습니다.\n다시 시도해주세요."]; //실패 후 처리
    }
}