import moment from 'moment';

export function formatDate(date, format = 'YYYY.MM.DD HH:mm:ss') {
    return moment(new Date(date)).format(format);
};