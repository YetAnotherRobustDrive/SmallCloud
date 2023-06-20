import RefreshToken from "../token/RefreshToken";
 

export default async function PostNewSegments(value, fileId) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    
    try {
        const upload = async () => {
            return new Promise((resolve, reject) => {
                const xhr = new XMLHttpRequest();
                xhr.open('POST', localStorage.getItem("API_SERVER") + 'segments/' + fileId, true);
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status !== 200) {       
                            reject({
                                status: xhr.status,
                            })
                        }
                        else {
                            resolve({
                                status: 200
                            });
                        }
                    }
                }
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
                xhr.send(value);
            });
        }

        const res = await upload();
        if (res.status === 200) {
            return [true, ''];  //성공
        }
        else {
            throw res; //실패
        }
    } catch (e) {
        return [false, "업로드에 실패했습니다.\n다시 시도해주세요."]; //실패 후 처리
    }
}