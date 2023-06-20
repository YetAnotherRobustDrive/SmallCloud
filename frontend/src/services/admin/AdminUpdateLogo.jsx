import jwtDecode from "jwt-decode";
import RefreshToken from "../token/RefreshToken";
import AdminPostConfig from "../config/AdminPostConfig";
 

export default async function AdminUpdateLogo(value, isImgChanged) {
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

    try {
        if (isImgChanged) {
            const imgRes = await uploadImage();
            if (imgRes.status !== 200) {
                throw imgRes;
            }
        }
        const res = await AdminPostConfig('logo', value.name);
        if (!res[0]) {
            throw res[1];
        }
        return [true, ''];
    } catch (e) {
        return [false, e.message];
    }
}