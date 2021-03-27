package bll.entities;



public interface ITransaction extends IOperation {


    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    ITransaction clone();
}
