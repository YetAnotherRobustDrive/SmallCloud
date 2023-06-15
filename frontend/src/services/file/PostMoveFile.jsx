import RefreshToken from "../token/RefreshToken";
 
import SwalError from "../../component/swal/SwalError";

export default async function PostMoveFile(sourceID, targetID) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "directoryId": targetID,
        })
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + "files/" + sourceID + '/move', model);
        if (res.status === 200) {
            return [true, ''];  //성공
        }
        else {
            const data = await res.json();
            throw data; //실패
        }
    } catch (e) {
        SwalError(e.message);
        return [false, e.message]; //실패 후 처리
    }
}