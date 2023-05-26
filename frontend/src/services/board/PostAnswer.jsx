import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function PostAnswer(value) {
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
        const res = await fetch(configData.API_SERVER + 'inquiries/answer', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (data) {
        return [false, data.message != undefined ? data.message : data];
    }
}