import { useState } from "react";


const Selection = (props) => {
    const {label, errorMessage, occupations} = props

    return(
        <div>
            <label>{label}</label>
            <select
                occupations={occupations}
            />
            <span>{errorMessage}</span>
        </div>
    )

};
export default Selection