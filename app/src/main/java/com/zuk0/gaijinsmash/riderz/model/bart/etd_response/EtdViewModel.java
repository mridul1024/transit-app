package com.zuk0.gaijinsmash.riderz.model.bart.etd_response;

import java.util.List;

public class EtdViewModel {

    private List<EtdXmlResponse> mList;

    public EtdViewModel(List<EtdXmlResponse> list) {
        mList = list;
    }

    public List<EtdXmlResponse> getList() { return mList; }
}
