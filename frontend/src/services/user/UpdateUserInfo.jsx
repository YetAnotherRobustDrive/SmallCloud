import jwtDecode from "jwt-decode";
import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function UpdateUserInfo(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");

    const uploadImage = async () => {
        const formData = new FormData();
        formData.append('imageFile', value.location);
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', configData.API_SERVER + 'users/profile-photo', true);
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status !== 200) {
                        resolve(xhr.responseText);
                    }
                    else {
                        const tRes = {
                            "status": 200,
                            "data": xhr.responseText
                        }
                        resolve(tRes);
                    }
                }
            }
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
            xhr.send(formData);
        });
    }

    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
        body: JSON.stringify({
            "name": value.nickname,
            "id": value.username,
        })
    };

    try {
        const imgRes = await uploadImage();
        if (imgRes.status !== 200) {
            throw imgRes;
        }

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