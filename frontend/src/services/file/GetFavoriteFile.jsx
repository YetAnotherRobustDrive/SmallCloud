import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function GetTrashFavoriteList(id) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + "files/" + id + '/files', model);
        const data = await res.json();
        data.forEach(e => {
            const size = parseInt(e.size);
            if (parseInt(size / Math.pow(10, 9)) > 0) {
                e.size = (size / Math.pow(10, 9)).toFixed(1) + "GB";
            }
            else if (parseInt(size / Math.pow(10, 6)) > 0) {
                e.size = (size / Math.pow(10, 6)).toFixed(1) + "MB";
            }
            else if (parseInt(size / Math.pow(10, 3)) > 0) {
                e.size = (size / Math.pow(10, 3)).toFixed(1) + "KB";
            }
            else {
                e.size = size + "B";
            }
            e.type = "file"
            e.isFavorite = e.labels.find("!$@*%&Favorite");
            e.labels = e.labels.filter(e => e !== "!$@*%&Favorite");
        });
        
        if (res.status === 200) {
            return [true, data];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}