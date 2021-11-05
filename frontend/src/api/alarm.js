import { axiosService } from '@/api/index.js';

function removeAlarm(data, callback, errorCallback) {
    axiosService
        .put('/alarm/check/', Object.values(data.checkList),
        )
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

function removeAllAlarm(memberId, callback, errorCallback) {
    
    axiosService
        .put('/alarm/checkAll/' + memberId)
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

export { removeAlarm, removeAllAlarm };
