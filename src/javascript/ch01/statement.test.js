const {describe, expect, test} = require('@jest/globals');
const statement = require('./statement');
const fs = require('fs');

const plays = JSON.parse(fs.readFileSync('./ch01/plays.json', 'utf8'));
const invoice = JSON.parse(fs.readFileSync('./ch01/invoices.json', 'utf8'));

describe('test statement function', () => {
    test('공연료 청구서를 출력하라', () => {
        const result = statement(invoice[0], plays);
        const expected = `
청구 내역 (고객명: BigCo)
 Hamlet: $650.00 (55석)
 As You Like It: $580.00 (35석)
 Othello: $500.00 (40석)
총액: $1,730.00
적립 포인트: 47점
`.trimStart();
        expect(result).toBe(expected);
    });
});
