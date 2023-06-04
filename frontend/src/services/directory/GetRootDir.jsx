import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function GetRootDir() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'users/root-dir' , model);
        const data = await res.json();
        if (res.status === 200) {
            return [true, data.result];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}