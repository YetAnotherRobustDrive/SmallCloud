import RefreshToken from '../token/RefreshToken';

export default async function PostBoard(value) {
    const accessToken = localStorage.getItem("accessToken");
    let model;
    if (accessToken === null) {
        model = {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(value),
        };        
    }
    else {
        model = {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + accessToken,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(value),
        };        
    }

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'inquiries', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        return [true, ''];
    } catch (data) {
        return data.message === undefined ? [false, data] : [false, data.message];
    }
}