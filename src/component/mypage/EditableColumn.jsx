import { TbEdit } from 'react-icons/tb'
import '../../css/mypage.css'

export default function EditableColumn(props) {

    const handleSubmit = (e) => {//todo
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        props.onSubmit(value.userInput);
    }

    return (
        <form className='editable-column' onSubmit={handleSubmit}>
            <div className="texts">
                <span className="title">{props.title}</span>
                <input 
                name="userInput"
                className="value" 
                defaultValue={props.value}
                type="text"
                size="10" />
            </div>
            {(props.editable != "false") &&
                <button type="submit" className="icon" ><TbEdit /></button>
            }
        </form>
    )
}