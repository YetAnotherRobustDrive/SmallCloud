import configData from "../../config/config.json"
import RefreshToken from "./RefreshToken";

export default async function IsAdminToken() {//stub
    await RefreshToken();

    try {
        //const accessToken = localStorage.getItem("accessToken");
        // const res = await fetch(configData.API_SERVER + 'auth/privileged', {
        //     method: "GET",
        //     headers: {
        //         "Authorization": "Bearer " + accessToken,
        //     },
        // })
        // const data = await res.json();
        return true;
    } catch (error) {
        return false;
    }
}