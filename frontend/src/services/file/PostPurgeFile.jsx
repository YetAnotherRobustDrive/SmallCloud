import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"
import PostUnfavoriteFile from "./PostUnfavoriteFile";

export default async function PostPurgeFile(fileID) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        await PostUnfavoriteFile(fileID);
        const res = await fetch(configData.API_SERVER + 'files/' + fileID + '/purge', model);
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