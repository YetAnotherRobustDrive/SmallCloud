import React, { useEffect, useState } from "react";
import { BsFillCircleFill } from "react-icons/bs";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import GetLoginLog from "../../services/log/GetLoginLog";

export default function SecurityInfoPage() {

    const [log, setLog] = useState([]);

    useEffect(() => {
        const init = async () => {
            const res = await GetLoginLog();
            setLog(res[1]);
        }
        init();
    }, []);

    return (
        <>
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <BodyHeader text="로그인 내역" />
                <div className="logTable">
                    <table>
                        <thead>
                            <tr>
                                <th>No</th>
                                <th>시간</th>
                                <th>종류</th>
                                <th>IP</th>
                                <th>결과</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                log.map((item, index) => (
                                    <tr key={index}>
                                        <td>{index}</td>
                                        <td>{item.localDateTime}</td>
                                        <td>{item.action}</td>
                                        <td>{item.ipAddr}</td>
                                        <td>{item.status === true ? <BsFillCircleFill color="green" /> : <BsFillCircleFill color="red" />}</td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
            </BodyFrame>
        </>
    )
}
