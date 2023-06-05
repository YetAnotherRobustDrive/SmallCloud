import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function PostDeleteGroup(name) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        }
    };

    try {
        const res = await fetch(configData.API_SERVER + 'group/' + name + '/delete', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (data) {
        return [false, data.message];
    }
}