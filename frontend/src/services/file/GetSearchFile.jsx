import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function GetSearchFile(str) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'files/search?q=' + str, model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        const value = data.result;
        value.forEach(e => {
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
            e.type = "file";
            e.isFavorite = e.labels.find(e => e.name === "!$@*%&Favorite") !== undefined;
            e.isShareExist = e.shares.length !== 0;
            e.labels = e.labels.filter(e => e.name.startsWith("!$@*%&") === false);
        });
        return [true, value];
    } catch (data) {
        return [false, data.message];
    }
}