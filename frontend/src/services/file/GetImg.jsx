import RefreshToken from "../token/RefreshToken";
 

export default async function GetImg(targetID, setter) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    

    try {
        const upload = async () => {
            return new Promise((resolve, reject) => {
                setter(1);
                const xhr = new XMLHttpRequest();
                xhr.responseType = 'blob';
                xhr.open('GET', localStorage.getItem("API_SERVER") + 'files/' + targetID, true);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        const res = xhr.response;
                        if (xhr.status !== 200) {      
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
                xhr.send();
            });
        }

        const res = await upload();
        if (res.status === 200) {
            return [true, res.data];  //성공
        }
        else {
            throw res; //실패
        }
    } catch (e) {
        return [false, "업로드에 실패했습니다.\n다시 시도해주세요."]; //실패 후 처리
    } finally {
        setter(0);
    }
}