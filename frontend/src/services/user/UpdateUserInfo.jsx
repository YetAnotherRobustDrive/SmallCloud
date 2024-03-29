import jwtDecode from "jwt-decode";
import RefreshToken from "../token/RefreshToken";
 

export default async function UpdateUserInfo(value, isImgChanged) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");

    const uploadImage = async () => {
        const formData = new FormData();
        formData.append('imageFile', value.location);
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', localStorage.getItem("API_SERVER") + 'users/profile-photo', true);
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
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "nickname": value.nickname,
            "username": value.username,
        })
    };

    try {
        if (isImgChanged) {
            const imgRes = await uploadImage();
            if (imgRes.status !== 200) {
                throw imgRes;
            }
        }

        const userID = jwtDecode(accessToken).sub;

        const res = await fetch(localStorage.getItem("API_SERVER") + 'users/' + userID + '/update', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (e) {
        return [false, e.message];
    }
}