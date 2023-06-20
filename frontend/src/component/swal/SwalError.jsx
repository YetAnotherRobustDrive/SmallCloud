import Swal from 'sweetalert2';

export default function SwalError(error) {
    const errorConverter = {
        "Failed to fetch": "서버와 연결할 수 없습니다.",
    }
    
    Swal.fire({
        icon: 'error',
        text: errorConverter[error] !== undefined ? errorConverter[error] : error,
        confirmButtonText: '확인',
        allowOutsideClick: false,
    })
}