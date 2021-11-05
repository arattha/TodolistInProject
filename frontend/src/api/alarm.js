import { axiosService } from '@/api/index.js';

function removeAlarm(data, callback, errorCallback) {
    axiosService
        .put('/alarm/check/' + data.memberId, Object.values(data.checkList),
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
        .put('/delAll', { params: { memberId: memberId } })
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

export { removeAlarm, removeAllAlarm };
