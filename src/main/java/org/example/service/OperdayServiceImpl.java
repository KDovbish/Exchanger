package org.example.service;

import org.example.entity.OperdayEntity;
import org.example.repositary.OperdayRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperdayServiceImpl implements OperdayService {

    @Autowired
    OperdayRepositary operdayRepositary;

    @Override
    public Boolean open() {
        OperdayEntity od = operdayRepositary.getOne(1);
        if ( (od.getOpensign() == false && od.getClosesign() == false) || (od.getOpensign() == true && od.getClosesign() == true) )
        {
            od.setOpensign(true);
            od.setClosesign(false);
            operdayRepositary.save(od);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean close() {
        OperdayEntity od = operdayRepositary.getOne(1);
        if ( od.getOpensign() == true && od.getClosesign() == false) {
            od.setClosesign(true);
            operdayRepositary.save(od);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isOpen() {
        OperdayEntity od = operdayRepositary.getOne(1);
        if (od.getOpensign() == true && od.getClosesign() == false) return true; else return false;
    }

}
