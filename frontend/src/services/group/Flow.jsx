import React, { useCallback, useEffect, useRef, useState } from 'react';
import { BiAddToQueue, BiSave } from 'react-icons/bi';
import ReactFlow, {
    ControlButton, Controls,
    addEdge, applyNodeChanges,
    updateEdge
} from 'reactflow';

import 'reactflow/dist/style.css';
import GetGroupTree from './GetGroupTree';
import PostCreateGroup from './PostCreateGroup';
import PostDeleteGroup from './PostDeleteGroup';
import SwalError from '../../component/swal/SwalError';
import SwalAlert from '../../component/swal/SwalAlert';

export default function Flow(props) {
    const [nodes, setNodes] = useState([]);
    const [edges, setEdges] = useState([]);
    const [prevEdges, setPrevEdges] = useState([]);

    const [isRootExist, setIsRootExist] = useState(false);

    const edgeUpdateSuccessful = useRef(true);
    const edgeRef = useRef();
    const nodeRef = useRef();
    const isRootExistRef = useRef();
    const tempRef = useRef();

    isRootExistRef.current = isRootExist;
    edgeRef.current = edges;
    nodeRef.current = nodes;

    useEffect(() => {
        const init = async () => {
            let tmpNodes = [];
            const res = await GetGroupTree();
            res.find(elem => {
                if (elem.source === "__ROOT__") {
                    tmpNodes = [{
                        id: elem.target,
                        type: 'customRootNode',
                        position: { x: 150, y: 0 },
                        data: {
                            label: elem.target,
                        }
                    }];
                    setIsRootExist(true);
                }
                return null;
            });

            const tmpRes = JSON.parse(JSON.stringify(res.filter(elem => elem.source !== "__ROOT__")));
            const tmpResForNode = JSON.parse(JSON.stringify(res.filter(elem => elem.source !== "__ROOT__")));

            tmpRes.forEach((item, index, object) => {
                if (item.source === "__ROOT__") {
                    object.splice(index, 1);
                    return;
                }
                item.id = "reactflow__edge-" + item.source + '-' + item.target;
                delete item.x;
                delete item.y;
            });
            setEdges(tmpRes);
            setPrevEdges(tmpRes);

            for (let index = 0; index < tmpResForNode.length; index++) {
                const elem = tmpResForNode[index];
                tmpNodes = [...tmpNodes, {
                    id: elem.target,
                    type: 'customNode',
                    position: { x: elem.x, y: elem.y },
                    data: {
                        label: elem.target,
                    }
                }]
            }
            setNodes(tmpNodes);
        }
        init();
    }, [])

    const onConnect = useCallback((params) => {
        const exist = edgeRef.current.find((e) => e.target === params.target);
        if (params.source === params.target) {
            return false;
        }
        if (exist !== undefined) {
            return false;
        }
        return setEdges((eds) => addEdge({ source: params.source, target: params.target }, eds));
    }, [setEdges]);
    const onNodesChange = useCallback((changes) => {
        if (tempRef.current === true) {
            tempRef.current = false;
            return;
        }
        else {
            tempRef.current = false;
        }
        setNodes((nds) => applyNodeChanges(changes, nds))
    }, []);
    const onEdgesChange = useCallback((changes) => {
        if (changes[0].type === "remove") {
            if (!window.confirm("그룹을 삭제하시겠습니까?")) {
                tempRef.current = true;
                return;
            }
        }
        else {
            tempRef.current = false;
        }
        setEdges((eds) => applyNodeChanges(changes, eds))
    }, []);
    const onNodesDelete = useCallback(
        (deleted) => {
            if (deleted[0].type === "customRootNode") {
                setIsRootExist(false);
            }
        },
        []
    );
    const onEdgeUpdateStart = useCallback(() => {
        edgeUpdateSuccessful.current = false;
    }, []);
    const onEdgeUpdate = useCallback((oldEdge, newConnection) => {
        edgeUpdateSuccessful.current = true;
        setEdges((els) => updateEdge(oldEdge, newConnection, els));
    }, []);
    const onEdgeUpdateEnd = useCallback((_, edge) => {
        if (!edgeUpdateSuccessful.current) {
            setEdges((eds) => eds.filter((e) => e.id !== edge.id));
        }
        edgeUpdateSuccessful.current = true;
    }, []);

    const handleClickAdd = () => {
        const rawName = window.prompt("새 그룹의 이름을 입력해주세요.");
        const reg = /[{}[]\/?.,;:|\)*~`!^-_+<>@#$%&\\=\('"]/gi;
        const name = rawName.replace(reg, '');
        if (name === null || name === "") {
            return;
        }
        const exist = nodeRef.current.find((e) => e.id === name);
        if (exist !== undefined) {
            SwalError("이미 존재하는 그룹입니다.")
            return;
        }

        if (isRootExistRef.current === true) {
            const newNode = nodes.concat({
                id: name,
                type: 'customNode',
                position: { x: 0, y: 0 },
                data: {
                    label: name,
                }
            })
            setNodes(newNode);
            return;
        }
        else {
            const newNode = nodes.concat({
                id: name,
                type: 'customRootNode',
                position: { x: 250, y: 0 },
                data: {
                    label: name,
                }
            })
            setIsRootExist(true);
            setNodes(newNode);
            return;
        }
    }

    const handleClickSave = async () => {
        const tmpEdge = JSON.parse(JSON.stringify(edgeRef.current));

        const rootNode = nodeRef.current.find((e) => e.type === "customRootNode");
        if (rootNode === undefined) {
            SwalError("루트 그룹이 존재하지 않습니다.");
            return;
        }
        await PostCreateGroup(rootNode.data.label, "__ROOT__");

        const createList = tmpEdge.filter(e => {
            if (prevEdges.find(elem => (elem.source === e.source && elem.target === e.target)) === undefined) {
                return true;
            }
            return false;
        })
        for (let index = 0; index < createList.length; index++) {
            const element = createList[index];
            await PostCreateGroup(element.target, element.source);
        }

        const deleteList = prevEdges.filter(e => {
            if (tmpEdge.find(elem => (elem.source === e.source && elem.target === e.target)) === undefined) {
                return true;
            }
            return false;
        })
        for (let index = 0; index < deleteList.length; index++) {
            const element = deleteList[index];
            await PostDeleteGroup(element.target);
        }
        SwalAlert("success", "그룹이 저장되었습니다.", () => { window.location.reload(); })
    }

    return (
        <div style={{ width: 'calc(100vw - 220px)', height: 'calc(100vh - 75px)' }}>
            <ReactFlow
                nodes={nodes}
                edges={edges}
                onNodesChange={onNodesChange}
                onEdgesChange={onEdgesChange}
                snapToGrid
                onEdgeUpdate={onEdgeUpdate}
                onEdgeUpdateStart={onEdgeUpdateStart}
                onEdgeUpdateEnd={onEdgeUpdateEnd}
                onConnect={onConnect}
                nodeTypes={props.nodeTypes}
                onNodesDelete={onNodesDelete}
            >
                <Controls style={{ width: "fit-content" }} showInteractive={false}>
                    <ControlButton onClick={handleClickAdd}>
                        <div><BiAddToQueue /></div>
                    </ControlButton>
                    <ControlButton onClick={handleClickSave}>
                        <div><BiSave /></div>
                    </ControlButton>
                </Controls>
            </ReactFlow>
        </div>
    );
}