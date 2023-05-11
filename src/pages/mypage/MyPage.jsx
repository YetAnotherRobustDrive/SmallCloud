import { useDispatch, useSelector } from "react-redux";
import ModalCheckPW from "../../component/modal/ModalCheckPw";
import { asyncSetPrivilege } from "../../slice/UserSlice";
import { useEffect } from "react";

export default function MyPage(props) {
    const dispatch = useDispatch();
    const isPrivileged = useSelector(state => state.user.isPrivileged);

    useEffect(() => {
        dispatch(asyncSetPrivilege());
    }, [])

    const render = () => {
        if (!isPrivileged) {
            return (
                <ModalCheckPW
                    isOpen={!isPrivileged}
                    after={() => {
                        dispatch(asyncSetPrivilege());
                    }} />
            )
        }
        else {
            return props.link;
        }
    }
    return render();
}