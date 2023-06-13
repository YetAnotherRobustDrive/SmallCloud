import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function ThrowPingAs(message) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'ping/' + message, model);
        if (res.status === 200) {
            return [true, ''];  //성공
        }
        else {
            return [true, ''];  //성공
        }
    } catch (e) {
        return [true, ''];  //성공
    }
}