package com.challenge.aspire.base

/**
 * @author LongHV.
 */
interface BasePresenter<V : BaseView> {

    fun onAttach(view: V)

    fun onDetach()

}
