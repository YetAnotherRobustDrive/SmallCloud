import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function GetGroup(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(value)
    };

    try {//todo
        // const res = await fetch(configData.API_SERVER + 'auth/elevate', model);
        // const data = await res.json();
        // if (!res.ok) {
        //     throw data;
        // }
        return [
            {"source": "a", "target": "b", "x": 100, "y": 150},
            {"source": "a", "target": "c", "x": 250, "y": 150},
            {"source": "a", "target": "d", "x": 400, "y": 150},
            {"source": "b", "target": "g", "x": 100, "y": 300},
            {"source": "c", "target": "e", "x": 250, "y": 300},
            {"source": "d", "target": "f", "x": 400, "y": 300},
        ]
    } catch (data) {
        return [false, data.message];
    }
}