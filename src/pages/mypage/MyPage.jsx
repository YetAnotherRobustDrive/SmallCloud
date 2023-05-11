import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import ModalCheckPW from "../../component/modal/ModalCheckPw";
import IsPrivilegedToken from "../../services/token/IsPrivilegedToken";
import { selectIsPrivileged, setPrivilege } from "../../slice/UserSlice";

export default function MyPage(props) {

    const isPrivilege = useSelector(selectIsPrivileged);
    const dispatch = useDispatch();

    const updatePrivilege = async () => {
        const res = await IsPrivilegedToken();
        dispatch(setPrivilege({ res }));
    }

    useEffect(() => {
        updatePrivilege();
    }, [])

    const render = () => {
        if (!isPrivilege) {
            return (
                <ModalCheckPW
                    isOpen={!isPrivilege}
                    after={() => {
                        updatePrivilege();
                    }} />
            )
        }
        else {
            return props.link;
        }
    }

    return (
        <>
            {render()}
        </>
    )
}