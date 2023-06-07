import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function PostDeleteShare(fileID, targetName, type) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
       
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "fileId": fileID,
            "targetName": targetName,
            "type": type,
        }),
    };

    try {
        const res = await fetch(configData.API_SERVER + "share/delete ", model);
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