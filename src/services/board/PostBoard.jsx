import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function PostBoard(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(value),
    };

    try {
        const res = await fetch(configData.API_SERVER + 'inquiries', model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        return [true, ''];
    } catch (data) {
        return [false, data.message];
    }
}