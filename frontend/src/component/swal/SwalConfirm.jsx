import Swal from 'sweetalert2';

export default function SwalConfirm(text, onConfirm, onCancel) {

    Swal.fire({
        title: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        reverseButtons: true
    }).then(async (result) => {
        if (result.isConfirmed) {
            onConfirm();
        } else if (
            result.dismiss === Swal.DismissReason.cancel
        ) {
            onCancel();
        }
    })
}