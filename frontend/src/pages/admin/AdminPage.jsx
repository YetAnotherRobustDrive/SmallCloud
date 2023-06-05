import { useDispatch } from "react-redux";
import { asyncCheckAdmin } from "../../slice/TokenSlice";
import { useNavigate } from 'react-router-dom';
import IsAdminToken from "../../services/token/IsAdminToken";

export default function AdminPage() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const render = async () => {
        const isAdmin = await IsAdminToken();
        if (!isAdmin) { //fail
            navigate('/');
            return;
        }
        dispatch(asyncCheckAdmin()); 
    }
    render();           
}