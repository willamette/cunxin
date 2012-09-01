package org.cunxin.app.model.payment.payee;

public abstract class Payee {
    //The amount unit will be CNY
    private double _desiredAmount = 0.0;
    private double _collectedAmount = 0.0;

    protected Payee(double desiredAmount) {
        _desiredAmount = desiredAmount;
    }

    public abstract String getPayeeAccount();

    public double getDesiredAmount() {
        return _desiredAmount;
    }

    public double getCollectedAmount() {
        return _collectedAmount;
    }

    public boolean isFilled() {
        return _collectedAmount >= _desiredAmount;
    }
}
