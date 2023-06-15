import Swal from 'sweetalert2';

export default function SwalError(error) {
    const errorConverter = [

    ]
    
    Swal.fire({
        icon: 'error',
        text: errorConverter.find((data) => data.origin === error.message) === undefined ? error.message : errorConverter.find((data) => data.origin === error.message).message,
        confirmButtonText: '확인',
        allowOutsideClick: false,
    })
}