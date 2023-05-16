import { TbEdit } from 'react-icons/tb'
import '../../css/mypage.css'

export default function EditableColumn(props) {

<<<<<<< HEAD
    const handleSubmit = (e) => {//todo
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        props.onSubmit(value.userInput);
    }

=======
>>>>>>> 1f55c100adcd46e865d9aa9d905f05b60b61bc57
    return (
        <div className='editable-column'>
            <div className="texts">
                <span className="title">{props.title}</span>
                <input 
                name={props.name}
                className="value" 
                defaultValue={props.value}
                type="text"
                size="10" 
                disabled={props.disabled}/>
            </div>
            {(props.title == "PW") &&
                <button onClick={props.onClick} className="icon" ><TbEdit /></button>
            }
        </div>
    )
}