import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function GetNoReplyBoard(id) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'inquiries' , model);
        const data = res.json();
        if (res.status == 200) {
            return [true, data];  //성공
        }
        else if (res.status == 404) {
            return [true, null];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}