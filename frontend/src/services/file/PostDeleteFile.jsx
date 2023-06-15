import RefreshToken from "../token/RefreshToken";
 
import PostUnfavoriteFile from "./PostUnfavoriteFile";

export default async function PostDeleteFile(fileID) {
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
        const res = await fetch(localStorage.getItem("API_SERVER") + 'files/' + fileID + '/delete', model);
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