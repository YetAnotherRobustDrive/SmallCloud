import React, { useCallback, useEffect, useRef, useState } from 'react';
import { BiAddToQueue, BiSave } from 'react-icons/bi';
import ReactFlow, {
    addEdge, applyNodeChanges, ControlButton, Controls, getConnectedEdges, getIncomers, getOutgoers, getRectOfNodes, updateEdge
} from 'reactflow';

import 'reactflow/dist/style.css';
import PostCreateGroup from './PostCreateGroup';
import GetGroupTree from './GetGroupTree';
import PostDeleteGroup from './PostDeleteGroup';

export default function Flow(props) {
    const [nodes, setNodes] = useState([]);
    const [edges, setEdges] = useState([]);
    const [prevEdges, setPrevEdges] = useState([]);

    const [isRootExist, setIsRootExist] = useState(false);

    const edgeUpdateSuccessful = useRef(true);
    const edgeRef = useRef();
    const nodeRef = useRef();
    const isRootExistRef = useRef();

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
                        },
                        dragHandle: "none"
                    }];
                    setIsRootExist(true);
                }
            });

            const tmpRes = JSON.parse(JSON.stringify(res.filter(elem => elem.source !== "__ROOT__")));
            const tmpResForNode = JSON.parse(JSON.stringify(res.filter(elem => elem.source !== "__ROOT__")));

            tmpRes.forEach((item, index, object) => {
                if (item.source === "__ROOT__") {
                    object.splice(index, 1);
                    return;
                }
                item.type = "step";
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
        if (exist !== undefined) {
            return false;
        }
        return setEdges((eds) => addEdge({ source: params.source, target: params.target, type: "step" }, eds));
    }, [setEdges]);
    const onNodesChange = useCallback((changes) => setNodes((nds) => applyNodeChanges(changes, nds)), []);
    const onEdgesChange = useCallback((changes) => setEdges((eds) => applyNodeChanges(changes, eds)), []);
    const onNodesDelete = useCallback(
        (deleted) => {
            if (deleted[0].type === "customRootNode") {
                setIsRootExist(false);
            }
            setEdges(
                deleted.reduce((acc, node) => {
                    const incomers = getIncomers(node, nodes, edges);
                    const outgoers = getOutgoers(node, nodes, edges);
                    const connectedEdges = getConnectedEdges([node], edges);

                    const remainingEdges = acc.filter((edge) => !connectedEdges.includes(edge));

                    const createdEdges = incomers.flatMap(({ id: source }) =>
                        outgoers.map(({ id: target }) => ({ source, target, type: "step" }))
                    );

                    return [...remainingEdges, ...createdEdges];
                }, edges)
            );
        },
        [nodes, edges]
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
        const reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
        const name = rawName.replace(reg, '');
        if (name === null || name === "") {
            return;
        }
        const exist = nodeRef.current.find((e) => e.id === name);
        if (exist !== undefined) {
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
                },
                dragHandle: "none"
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
        alert("저장되었습니다.");
        window.location.reload();
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