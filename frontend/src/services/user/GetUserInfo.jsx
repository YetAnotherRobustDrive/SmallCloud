import jwtDecode from "jwt-decode";
import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function GetUserInfo() {
    await RefreshToken();

    try {
        const accessToken = localStorage.getItem("accessToken");
        const userID = jwtDecode(accessToken).sub;
        const res = await fetch(configData.API_SERVER + 'users/' + userID, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + accessToken,
            },
        })
        const data = await res.json();
        return data;
    } catch (error) {
        return false;
    }
}