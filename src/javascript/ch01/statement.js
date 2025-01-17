function renderPlainText(data, plays) {
    let result = `청구 내역 (고객명: ${data.customer})\n`;
    const usd = new Intl.NumberFormat("en-US",
        {
            style: "currency", currency: "USD",
            minimumFractionDigits: 2
        }).format;

    function amountFor(aPerformance) {
        let result = 0;
        switch (playFor(aPerformance).type) {
            case "tragedy":
                result = 40000;
                if (aPerformance.audience > 30) {
                    result += 1000 * (aPerformance.audience - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (aPerformance.audience > 20) {
                    result += 10000 + 500 * (aPerformance.audience - 20);
                }
                result += 300 * aPerformance.audience;
                break;
            default:
                throw new Error(`알 수 없는 장르: ${playFor(aPerformance).type}`);
        }
        return result;
    }

    function playFor(aPerformance) {
        return plays[aPerformance.playID];
    }

    function volumeCreditsFor(aPerformance) {
        let volumeCredits = 0;
        volumeCredits += Math.max(aPerformance.audience - 30, 0);
        if ("comedy" === playFor(aPerformance).type) {
            volumeCredits += Math.floor(aPerformance.audience / 5);
        }
        return volumeCredits;
    }

    function totalVolumeCredits() {
        let result = 0;
        for (let perf of data.performances) {
            result += volumeCreditsFor(perf);
        }
        return result;
    }

    function totalAmount() {
        let result = 0;
        for (let perf of data.performances) {
            result += amountFor(perf);
        }
        return result;
    }

    for (let perf of data.performances) {
        result += ` ${playFor(perf).name}: ${usd(amountFor(perf) / 100)} (${perf.audience}석)\n`;
    }

    result += `총액: ${usd(totalAmount() / 100)}\n`;
    result += `적립 포인트: ${(totalVolumeCredits())}점\n`;
    return result;
}

function statement(invoice, plays) {
    const statementData = {};
    statementData.customer = invoice.customer;
    statementData.performances = invoice.performances.map(enrichPerformance);
    return renderPlainText(statementData, plays);

    function enrichPerformance(aPerformance) {
        const result = Object.assign({}, aPerformance);
        return result;
    }
}

module.exports = statement;
