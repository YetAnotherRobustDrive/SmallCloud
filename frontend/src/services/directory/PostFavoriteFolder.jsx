import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function PostFavoriteFolder(folderID) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'folder/' + folderID + '/favorite', model);
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