import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function GetSearchGroup(str) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'group/search?q=' + str, model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        return [true, data];
    } catch (data) {
        return [false, data.message];
    }
}