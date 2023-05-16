import configData from "../../config/config.json"
import RefreshToken from "./RefreshToken";

<<<<<<< HEAD
//todo
=======
>>>>>>> 1f55c100adcd46e865d9aa9d905f05b60b61bc57
export default async function IsAdminToken() {
    await RefreshToken();

    try {
        const accessToken = localStorage.getItem("accessToken");
        const res = await fetch(configData.API_SERVER + 'auth/admin-check', {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + accessToken,
            },
        })
        const data = await res.json();
        //return data.result;
        return true;
    } catch (error) {
        return false;
    }
}