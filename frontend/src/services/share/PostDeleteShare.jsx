import RefreshToken from "../token/RefreshToken";
import configData from "../../config.json";

export default async function PostDeleteShare(value) {
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
        const res = await fetch(configData.API_SERVER + "share/create", model);
        const data = await res.json();
        if (res.status === 200) {
            return [true, data];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }

}