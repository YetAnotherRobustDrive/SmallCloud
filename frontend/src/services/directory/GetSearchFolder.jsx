import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function GetSearchFolder(str) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'directory/search?q=' + str, model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        const value = data.result;
        value.forEach((e) => {
            e.type = "folder"
            e.isFavorite = e.labels.find(e => e.name === "!$@*%&Favorite") !== undefined;
            e.labels = e.labels.filter(e => e.name.startsWith("!$@*%&") === false);
        });
        const withoutRoot = value.filter(e => e.name !== "_ROOT_");
        return [true, withoutRoot];
    } catch (data) {
        return [false, data.message];
    }
}