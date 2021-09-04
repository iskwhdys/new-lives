package com.iskwhdys.newlives.pres;

import com.iskwhdys.newlives.domain.liver.LiverEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiverResponse extends LiverEntity {

    private static final long serialVersionUID = -9126434746762443929L;
    private String twitter2;
    private String youtube2;

}
