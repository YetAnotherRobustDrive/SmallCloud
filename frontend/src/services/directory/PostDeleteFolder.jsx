import RefreshToken from "../token/RefreshToken";
 
import PostUnfavoriteFolder from "./PostUnfavoriteFolder";

export default async function PostDeleteFolder(id) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        await PostUnfavoriteFolder(id);
        const res = await fetch(localStorage.getItem("API_SERVER") + 'directory/' + id + '/delete', model);
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