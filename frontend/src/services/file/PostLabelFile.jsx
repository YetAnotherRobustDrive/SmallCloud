import RefreshToken from "../token/RefreshToken";
 

export default async function PostLabelFile(fileID, value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "fileId" : fileID,
            "labels" : value,
        }),
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'files/update/label', model);
        if (res.status === 200) {
            return [true, ''];  //성공
        }
        else {
            const data = await res.json();
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}