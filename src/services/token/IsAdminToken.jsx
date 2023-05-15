import configData from "../../config/config.json"
import RefreshToken from "./RefreshToken";

//todo
export default async function IsAdminToken() {
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