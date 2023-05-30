import jwtDecode from "jwt-decode";
import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function UpdateUserInfo(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
        body: value,
    };

    try {
    
        const userID = jwtDecode(accessToken).sub;

        const res = await fetch(configData.API_SERVER + 'users/' + userID + '/update', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (e) {
        return [false, e.message];
    }
}