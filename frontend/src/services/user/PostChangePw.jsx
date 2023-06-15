import RefreshToken from '../token/RefreshToken';

export default async function PostChangePw(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(value)
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'users/password', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (data) {
        return [false, data.message !== undefined ? data.message : data];
    }
}