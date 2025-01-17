import createStatementData from "./createStatementData";

function renderPlainText(data, plays) {
    let result = `청구 내역 (고객명: ${data.customer})\n`;
    const usd = new Intl.NumberFormat("en-US",
        {
            style: "currency", currency: "USD",
            minimumFractionDigits: 2
        }).format;

    for (let perf of data.performances) {
        result += ` ${perf.play.name}: ${usd(perf.amount / 100)} (${perf.audience}석)\n`;
    }

    result += `총액: ${usd(data.totalAmount / 100)}\n`;
    result += `적립 포인트: ${data.totalVolumeCredits}점\n`;
    return result;
}

export default function statement(invoice, plays) {
    return renderPlainText(createStatementData(invoice, plays));
}
